package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.customer.Customer;
import com.udacity.jdnd.course3.critter.user.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired PetRepository petRepository;

    @Autowired CustomerRepository customerRepository;

    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Optional<Customer> customer = customerRepository.findById(petDTO.getOwnerId());
        if (customer.isPresent()) {
            Pet pet = new Pet();
            pet.setPetType(petDTO.getType());
            pet.setName(petDTO.getName());
            pet.setCustomer(customer.get());
            pet.setBirthDate(petDTO.getBirthDate());
            pet.setNotes(petDTO.getNotes());
            Pet savePet = petRepository.save(pet);
            return new PetDTO(savePet.getId(), savePet.getPetType(), savePet.getName(), savePet.getCustomer().getId(), savePet.getBirthDate(), savePet.getNotes());
        } else {
            return null;
        }
    }

    public PetDTO getPet(@PathVariable long petId) {
        Pet getPet = petRepository.findById(petId).get();
        return new PetDTO(getPet.getId(), getPet.getPetType(), getPet.getName(), getPet.getCustomer().getId(), getPet.getBirthDate(), getPet.getNotes());
    }

    public List<PetDTO> getPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOs.add(new PetDTO(
                    pet.getId(),
                    pet.getPetType(),
                    pet.getName(),
                    pet.getCustomer().getId(),
                    pet.getBirthDate(),
                    pet.getNotes()
            ));
        }
        return petDTOs;
    }

    List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOs.add(new PetDTO(
                    pet.getId(),
                    pet.getPetType(),
                    pet.getName(),
                    pet.getCustomer().getId(),
                    pet.getBirthDate(),
                    pet.getNotes()
            ));
        }
        return petDTOs;
    }
}
