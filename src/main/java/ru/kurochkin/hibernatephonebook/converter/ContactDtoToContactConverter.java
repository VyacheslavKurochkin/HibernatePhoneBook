package ru.kurochkin.hibernatephonebook.converter;

import org.springframework.stereotype.Service;
import ru.kurochkin.hibernatephonebook.dto.ContactDto;
import ru.kurochkin.hibernatephonebook.entity.Contact;

@Service
public class ContactDtoToContactConverter implements Converter<ContactDto, Contact> {
    @Override
    public Contact convert(ContactDto source) {
        return new Contact(source.getId(), source.getName(), source.getPhone());
    }
}