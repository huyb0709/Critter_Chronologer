package com.udacity.jdnd.course3.critter.user.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDayAvailablesContainingAndSkillsIn(DayOfWeek dayOfWeek, Set<EmployeeSkill> skills);
}
