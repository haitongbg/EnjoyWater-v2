package com.enjoywater.listener;

import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;

public interface AddressListener {
    void selectCity(City city);

    void selectDistrict(District district);

    void selectWard(Ward ward);
}
