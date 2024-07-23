package ru.kurochkin.hibernatephonebook.service;

import org.springframework.stereotype.Service;
import ru.kurochkin.hibernatephonebook.dao.ContactRepository;
import ru.kurochkin.hibernatephonebook.dto.BaseResponse;
import ru.kurochkin.hibernatephonebook.entity.Contact;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public List<Contact> findAll(String term) {
        return contactRepository.findAll(term);
    }

    public BaseResponse delete(Contact contact) {
        contactRepository.delete(contact);
        return BaseResponse.getSuccessResponse();
    }

    public BaseResponse save(Contact contact) {
        Integer id = contact.getId();

        if (id != null) {
            if (!contactRepository.existsById(id)) {
                return BaseResponse.getErrorResponse("Контакт с id = %d отсутствует в базе".formatted(id));
            }
        }

        String phone = contact.getPhone();
        Contact contactByPhone = contactRepository.findContactByPhone(phone);

        if (contactByPhone != null && id != null && !id.equals(contactByPhone.getId())) {
            return BaseResponse.getErrorResponse("Контакт с номером телефона = %s уже существует в базе".formatted(phone));
        }

        contactRepository.save(contact);
        return BaseResponse.getSuccessResponse();
    }
}
