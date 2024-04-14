package com.udacity.jdnd.course3.critter.user.customer;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired CustomerRepository customerRepository;

    @Autowired PetRepository petRepository;

    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO)   {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        Customer saveCus = customerRepository.save(customer);
        return new CustomerDTO(saveCus.getId(), saveCus.getName(), saveCus.getPhoneNumber(), saveCus.getNotes());
    }

    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOs.add(new CustomerDTO(
                    customer.getId(),
                    customer.getName(),
                    customer.getNotes(),
                    customer.getPhoneNumber()
            ));
        }
        return customerDTOs;
    }

    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent()) {
            Customer customer = customerRepository.findById(pet.get().getCustomer().getId()).orElse(null);
            return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes());
        } else {
            return null;
        }
    }

}
