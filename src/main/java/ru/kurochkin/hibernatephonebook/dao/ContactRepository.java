package ru.kurochkin.hibernatephonebook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kurochkin.hibernatephonebook.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Contact findByPhoneIgnoreCase(String phone);

    List<Contact> findByPhoneContainsOrNameContainsAllIgnoreCase(String phone, String name);
}