package ru.kurochkin.hibernatephonebook.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.kurochkin.hibernatephonebook.dao.ContactRepository;
import ru.kurochkin.hibernatephonebook.dto.BaseResponse;
import ru.kurochkin.hibernatephonebook.entity.Contact;

import java.util.List;

@Service
public class ContactService implements CrudService<Contact> {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> findAll(String term) {
        return contactRepository.findByPhoneContainsOrNameContainsAllIgnoreCase(term, term);
    }

    @Override
    public BaseResponse delete(Contact contact) {
        contactRepository.delete(contact);
        return BaseResponse.getSuccessResponse();
    }

    @Override
    @Transactional
    public BaseResponse delete(List<Integer> ids) {
        contactRepository.deleteAllByIdInBatch(ids);
        return BaseResponse.getSuccessResponse();
    }

    @Override
    @Transactional
    public BaseResponse save(Contact contact) {
        Integer id = contact.getId();

        if (id != null && !contactRepository.existsById(id)) {
            return BaseResponse.getErrorResponse("Контакт с id = %d отсутствует в базе".formatted(id));
        }

        String phone = contact.getPhone();
        Contact contactByPhone = contactRepository.findByPhoneIgnoreCase(phone);

        if (contactByPhone != null && id != null && !id.equals(contactByPhone.getId())) {
            return BaseResponse.getErrorResponse("Контакт с номером телефона = %s уже существует в базе".formatted(phone));
        }

        contactRepository.save(contact);
        return BaseResponse.getSuccessResponse();
    }
}
