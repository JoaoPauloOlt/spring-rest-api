package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CityNotFoundException;
import com.algaworks.algafood.domain.exception.EntityInUseException;
import com.algaworks.algafood.domain.model.City;
import com.algaworks.algafood.domain.model.State;
import com.algaworks.algafood.domain.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterCityService {

    public static final String MSG_CITY_IN_USE = "Code city %d cannot be removed as it is in use";

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RegisterStateService registerState;

    @Transactional
    public City save(City city) {
        Long stateId = city.getState().getId();

        State state = registerState.searchOrError(stateId);

        city.setState(state);
        return cityRepository.save(city);
    }

    @Transactional
    public void delete(Long cityId) {
        try {
            cityRepository.deleteById(cityId);
            cityRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new CityNotFoundException(cityId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(MSG_CITY_IN_USE, cityId));
        }
    }

    public City searchOrError(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));

    }
}