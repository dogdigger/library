package com.elias.reader.controller;

import com.elias.reader.service.AddPersonForm;
import com.elias.reader.service.Person;
import com.elias.reader.service.ReaderService;
import com.elias.reader.service.UpdatePersonForm;
import com.elias.reader.util.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @author chengrui
 * <p>create at: 2020-07-27 20:12</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping("/api/persons")
public class ReaderController {
    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService){
        this.readerService = readerService;
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable String id) {
        return readerService.getPerson(id);
    }

    @GetMapping
    public List<Person> getAllPerson() {
        return readerService.getAllPerson();
    }

    @PostMapping
    public Person addPerson(@RequestBody @Valid AddPersonForm addPersonForm) {
        Person p = new Person();
        BeanUtils.copyProperties(addPersonForm, p);
        p.setId(CommonUtils.generateRandomString(6));
        readerService.addPerson(p);
        return p;
    }

    @DeleteMapping("/{id}")
    public Person removePerson(@PathVariable String id) {
        return readerService.removePerson(id);
    }

    @PutMapping
    public Person updatePerson(@RequestBody @Valid UpdatePersonForm updatePersonForm) {
        Person p = new Person();
        BeanUtils.copyProperties(updatePersonForm, p);
        return readerService.updatePerson(p);
    }
}
