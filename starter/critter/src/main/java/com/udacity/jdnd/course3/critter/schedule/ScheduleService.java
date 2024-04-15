package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.employee.Employee;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    @Autowired ScheduleRepository scheduleRepository;

    @Autowired EmployeeRepository employeeRepository;

    @Autowired PetRepository petRepository;

    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        if (scheduleDTO.getEmployeeIds().isEmpty() || scheduleDTO.getPetIds().isEmpty()) {
            throw new IllegalArgumentException("List employee or pet can't empty!");
        }

        // Get list employee and pet
        List<Employee> employees = employeeRepository.findAllById(scheduleDTO.getEmployeeIds());
        List<Pet> pets = petRepository.findAllById(scheduleDTO.getPetIds());

        // Check employee and pet is exist
        if (employees.isEmpty() || pets.isEmpty()) {
            throw new IllegalArgumentException("Not found employee or pet!");
        }

        // Create and save new Schedule
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        Schedule savedSchedule = this.scheduleRepository.save(schedule);

        // Create DTO from schedule and save data return
        List<Long> petIds = savedSchedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        List<Long> employeeIds = savedSchedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        return new ScheduleDTO(
                savedSchedule.getId(),
                employeeIds,
                petIds,
                savedSchedule.getDate(),
                savedSchedule.getActivities()
        );
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()),
                        schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()),
                        schedule.getDate(),
                        schedule.getActivities()
                ))
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        List<Schedule> schedules = scheduleRepository.findAllByPetsContaining(pet);
        return schedules.stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()),
                        schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()),
                        schedule.getDate(),
                        schedule.getActivities()
                ))
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        List<Schedule> schedules = scheduleRepository.findAllByEmployeesContaining(employee);
        return schedules.stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()),
                        schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()),
                        schedule.getDate(),
                        schedule.getActivities()
                ))
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = petRepository.findByOwnerId(customerId);
        List<Schedule> schedules = scheduleRepository.findAllByPetsIn(pets);
        return schedules.stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()),
                        schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()),
                        schedule.getDate(),
                        schedule.getActivities()
                ))
                .collect(Collectors.toList());
    }
}
