package ru.kurochkin.hibernatephonebook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kurochkin.hibernatephonebook.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    @Query("select c from Contact c where c.phone = :phone")
    Contact findContactByPhone(String phone);

    @Query(value = "select c from Contact c where c.phone like %:term% OR c.name like %:term%")
    List<Contact> findAll(String term);
}