import axios from 'axios';

const CITY_API_BASE_URL = "http://localhost:8081/"

class CityService {

    createTableCity(queryString) {
        return axios.post(CITY_API_BASE_URL, {
            data: queryString
        });
    }

    selectFromCities(queryString) {
        return axios.get(CITY_API_BASE_URL + 'cities?queryString=SELECT * FROM City;');
    }

    selectFromCitiesByParameters(queryString) {
        return axios.get(CITY_API_BASE_URL + 'cities?queryString=' + queryString);
    }

    insertCity(queryString) {
        return axios.post(CITY_API_BASE_URL, {
            data: queryString
         });
    }

    getCityById(queryString) {
        return axios.get(CITY_API_BASE_URL + 'cityInfo?queryString=' + queryString);
    }

    updateCity(queryString) {
        return axios.put(CITY_API_BASE_URL + 'cityInfo', {
            data: queryString
        });
    }

    deleteCity = (parameter) => {
        return axios.delete(CITY_API_BASE_URL + 'cities?queryString=' + parameter);
    }
}

export default new CityService()