package ru.kurochkin.hibernatephonebook.controller;

import org.springframework.web.bind.annotation.*;
import ru.kurochkin.hibernatephonebook.converter.ContactDtoToContactConverter;
import ru.kurochkin.hibernatephonebook.converter.ContactToContactDtoConverter;
import ru.kurochkin.hibernatephonebook.dto.BaseResponse;
import ru.kurochkin.hibernatephonebook.dto.ContactDto;
import ru.kurochkin.hibernatephonebook.entity.Contact;
import ru.kurochkin.hibernatephonebook.service.ContactService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;
    private final ContactToContactDtoConverter contactToContactDtoConverter;
    private final ContactDtoToContactConverter contactDtoToContactConverter;

    public ContactController(ContactService contactService,
                             ContactToContactDtoConverter contactToContactDtoConverter,
                             ContactDtoToContactConverter contactDtoToContactConverter) {
        this.contactService = contactService;
        this.contactToContactDtoConverter = contactToContactDtoConverter;
        this.contactDtoToContactConverter = contactDtoToContactConverter;
    }

    @GetMapping
    public List<ContactDto> getAll(@RequestParam String term) {
        if (term != null && !term.isEmpty()) {
            return contactToContactDtoConverter.convert(contactService.findAll(term));
        }

        return contactToContactDtoConverter.convert(contactService.findAll());
    }

    @PostMapping
    public BaseResponse create(@RequestBody ContactDto contactDto) {
        if (contactDto.getName() == null || contactDto.getName().isEmpty()) {
            return BaseResponse.getErrorResponse("Создание контакта: объект должен содержать имя контакта");
        }

        if (contactDto.getPhone() == null || contactDto.getPhone().isEmpty()) {
            return BaseResponse.getErrorResponse("Создание контакта: объект должен содержать телефон контакта");
        }

        Contact contact = contactDtoToContactConverter.convert(contactDto);
        return contactService.save(contact);
    }

    @PutMapping
    public BaseResponse edit(@RequestBody ContactDto contactDto) {
        if (contactDto.getId() == null) {
            return BaseResponse.getErrorResponse("Изменение контакта: объект должен содержать id контакта");
        }

        if (contactDto.getName() == null || contactDto.getName().isEmpty()) {
            return BaseResponse.getErrorResponse("Изменение контакта: объект должен содержать имя контакта");
        }

        if (contactDto.getPhone() == null || contactDto.getPhone().isEmpty()) {
            return BaseResponse.getErrorResponse("Изменение контакта: объект должен содержать телефон контакта");
        }

        Contact contact = contactDtoToContactConverter.convert(contactDto);
        return contactService.save(contact);
    }

    @DeleteMapping
    public BaseResponse delete(@RequestBody List<Integer> ids) {
        if (ids == null) {
            return BaseResponse.getErrorResponse("Удаление контактов: не переданы id контактов");
        }

        return contactService.delete(ids);
    }
}
