package com.example.SpringDataRedisDemo.service;
import com.example.SpringDataRedisDemo.EmployeeDetailDTO;
import com.example.SpringDataRedisDemo.entity.Address;
import com.example.SpringDataRedisDemo.entity.Employee;
import com.example.SpringDataRedisDemo.exception.CardNotCreatedException;
import com.example.SpringDataRedisDemo.exception.laptopNotAllocatedException;
import com.example.SpringDataRedisDemo.repo.IEmployeeRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {

    private static Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired
    private IEmployeeRepo employeeRepo;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    Map<String, Employee> empLocalCache = new HashMap<>();

    private boolean cardCreated = false;
    private boolean laptopNotAllocated = true;

    // This @Transactional annotation makes this method to be executed in a transaction.
    // The transaction is committed only after the entire method is executed.
    @Transactional(rollbackOn = {CardNotCreatedException.class}, dontRollbackOn = {laptopNotAllocatedException.class})
    public Employee createEmployee(EmployeeDetailDTO employeeDetailDTO) throws CardNotCreatedException, laptopNotAllocatedException {
        Employee employee = Employee.builder().name(employeeDetailDTO.getName()).email(employeeDetailDTO.getEmail()).build();
        Address address = Address.builder().line1(employeeDetailDTO.getLine1()).line2(employeeDetailDTO.getLine2()).city(employeeDetailDTO.getCity()).build();
        employee.setAddress(address);
        Employee emp = employeeRepo.save(employee);
        // Service call for card creation
        // rollbackOn - rollback transaction when this exception occurs.
        if(!cardCreated)
        {
            throw new CardNotCreatedException();
        }
        // dontRollBackOn - transaction will not be rollback when this exception occurs
        if(laptopNotAllocated)
        {
            throw new laptopNotAllocatedException();
        }
        LOGGER.info("Created Employee : {}", emp);
        return emp;
    }

    public Employee getEmployees(Long id)
    {
//        // Using local cache (hashmap)
//        String key = "emp:"+id;
//        Employee employee = new Employee();
//        if(empLocalCache.containsKey(key))
//        {
//            employee = empLocalCache.get(key);
//        }
//        else {
//            employee = employeeRepo.findById(id).orElse(null);
//            if(employee != null)
//            {
//                empLocalCache.put("emp:"+employee.getId(), employee);
//            }
//        }
//        return employee;

        // Using Redis
        String key = "emp:"+id;
        Employee employee = (Employee) redisTemplate.opsForValue().get(key);
        if(employee == null)
        {
            employee = employeeRepo.findById(id).orElse(null);
            redisTemplate.opsForValue().set(key, employee);
        }
        return employee;
    }

    public Employee getEmployeeByEmail(String email)
    {
        return employeeRepo.findByEmail(email);
    }

    public Employee deleteEmployee(Long id)
    {
        Employee emp = getEmployees(id);
        employeeRepo.delete(emp);
        return emp;
    }

}
