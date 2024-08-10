package ru.kurochkin.hibernatephonebook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kurochkin.hibernatephonebook.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Contact findByPhoneIgnoreCase(String phone);

    List<Contact> findByPhoneContainsOrNameContainsAllIgnoreCase(String term1, String term2);

    @Query(value = "select c from Contact c where c.phone like %:term% OR c.name like %:term%")
    List<Contact> findAll(String term);
}