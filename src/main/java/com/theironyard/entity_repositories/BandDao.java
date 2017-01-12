package com.theironyard.entity_repositories;

import com.theironyard.bands_core.model.Band;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by alhanger on 12/8/15.
 */
public interface BandDao extends CrudRepository<Band, Integer> {

    List<Band> findAllByUserId(String id);

    Band findByEventsId(int id);

    Band findOneById(int id);

    Band findOneByName(String Name);

}
