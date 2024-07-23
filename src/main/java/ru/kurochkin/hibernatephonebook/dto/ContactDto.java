package ru.kurochkin.hibernatephonebook.dto;

import lombok.*;
import ru.kurochkin.hibernatephonebook.entity.Contact;

/**
 * DTO for {@link Contact}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactDto {
    private Integer id;
    private String name;
    private String phone;
}