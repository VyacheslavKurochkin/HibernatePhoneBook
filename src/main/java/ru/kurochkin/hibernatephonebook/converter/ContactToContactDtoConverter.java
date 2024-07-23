package ru.kurochkin.hibernatephonebook.converter;

import org.springframework.stereotype.Service;
import ru.kurochkin.hibernatephonebook.dto.ContactDto;
import ru.kurochkin.hibernatephonebook.entity.Contact;

@Service
public class ContactToContactDtoConverter implements Converter<Contact, ContactDto> {
    @Override
    public ContactDto convert(Contact source) {
        return new ContactDto(source.getId(), source.getName(), source.getPhone());
    }
}
