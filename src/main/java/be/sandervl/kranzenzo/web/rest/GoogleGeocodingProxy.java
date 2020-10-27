package be.sandervl.kranzenzo.web.rest;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class GoogleGeocodingProxy {

    private final RestTemplate restTemplate;

    public GoogleGeocodingProxy( RestTemplate restTemplate ) {
        this.restTemplate = restTemplate;
    }

//    https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyC4x6KxHhrA2XjAcOMCqeKBri1_fW-kdl4
//        &language=nl
//        &region=be
//        &components=country:be
//        &address=${encodeURIComponent( this.textInput )}

    @GetMapping(value = "/api/google/geocoding", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity <String> get( @RequestParam("term") String term ) {
        URI url = UriComponentsBuilder
            .fromHttpUrl( "https://maps.googleapis.com/maps/api/geocode/json" )
            .queryParam( "key", "AIzaSyC4x6KxHhrA2XjAcOMCqeKBri1_fW-kdl4" )
            .queryParam( "components", "country:be" )
            .queryParam( "address", term )
            .build().toUri();
        RequestEntity <?> request = RequestEntity.get( url ).build();
        return restTemplate.exchange( request, String.class );
    }
}
