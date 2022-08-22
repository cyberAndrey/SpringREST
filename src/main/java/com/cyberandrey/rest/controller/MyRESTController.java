package com.cyberandrey.rest.controller;

import com.cyberandrey.rest.entity.Employee;
import com.cyberandrey.rest.exception_handling.NoSuchEmployeeException;
import com.cyberandrey.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRESTController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> showAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        Employee employee = employeeService.getEmployee(id);

        if (employee==null)
            throw new NoSuchEmployeeException("No employee with id=" + id);

        return employee;
    }

    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id) {
        Employee employee = employeeService.getEmployee(id);
        if (employee==null)
            throw new NoSuchEmployeeException("No employee with id=" + id);

        employeeService.deleteEmployee(id);
        return "Employee with id=" + id + " was deleted";
    }

    @PostMapping("/employees")
    public Employee addNewEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        String URL = "s";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(URL,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>() {});
        ResponseEntity<String> response = restTemplate.postForEntity(URL, employee, String.class);
        return employee;
    }

}
