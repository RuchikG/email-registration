package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.Section;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SectionRepository extends CrudRepository<Section,String> {
    Section findBySectionNumber(String sectionNumber);
}
