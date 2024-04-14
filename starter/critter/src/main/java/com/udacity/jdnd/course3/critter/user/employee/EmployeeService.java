package com.udacity.jdnd.course3.critter.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class EmployeeService {

    @Autowired EmployeeRepository employeeRepository;

    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDayAvailables(employeeDTO.getDaysAvailable());
        Employee saveEmp = employeeRepository.save(employee);
        return new EmployeeDTO(saveEmp.getId(), saveEmp.getName(), saveEmp.getSkills(), saveEmp.getDayAvailables());
    }

    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee getEmployee = employeeRepository.findById(employeeId).get();
        return new EmployeeDTO(getEmployee.getId(), getEmployee.getName(), getEmployee.getSkills(), getEmployee.getDayAvailables());
    }

    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Optional<Employee> empId = employeeRepository.findById(employeeId);
        if (empId.isPresent()) {
            Employee employee = empId.get();
            employee.setDayAvailables(daysAvailable);
            employeeRepository.save(employee);
        } else {
            return;
        }
    }

    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        DayOfWeek dayOfWeek = employeeDTO.getDate().getDayOfWeek();
        List<Employee> result = employeeRepository.findByDayAvailablesContainingAndSkillsIn(dayOfWeek, employeeDTO.getSkills());

        return result.stream()
                .map(employee -> new EmployeeDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getSkills(),
                        employee.getDayAvailables()
                ))
                .collect(Collectors.toList());
    }
}
