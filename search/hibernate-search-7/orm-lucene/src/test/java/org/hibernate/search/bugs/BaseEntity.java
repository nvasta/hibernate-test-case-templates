package org.hibernate.search.bugs;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;

import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity <ID extends Serializable> {

    // Class Variables
    @Id
    @DocumentId
    ID id;

    /**
     * Gets id.
     *
     * @return the id
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(ID id) {
        this.id = id;
    }
}