package com.shop.admin.country;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Coutry Not Found")
public class CountryRestNotFoundException extends Exception {

}
