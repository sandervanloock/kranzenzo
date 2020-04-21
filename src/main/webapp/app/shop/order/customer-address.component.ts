import {Component, forwardRef, OnInit} from '@angular/core';
import {ICustomer} from 'app/shared/model/customer.model';
import {ControlValueAccessor, FormControl, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validator} from '@angular/forms';

declare var google: any;
declare var $: any;

@Component( {
                selector: 'jhi-customer-address', templateUrl: './customer-address.component.html', styleUrls: ['customer-order.css'], providers: [{
        provide: NG_VALUE_ACCESSOR, useExisting: forwardRef( () => CustomerAddressComponent ), multi: true
    }, {
        provide: NG_VALIDATORS, useExisting: forwardRef( () => CustomerAddressComponent ), multi: true
    }]
            } )
export class CustomerAddressComponent implements OnInit, ControlValueAccessor, Validator {
    textInput = '';

    private searchBox: any;
    private invalidAddressError = false;
    private output: ICustomer;

    private propagateChange = (_: any) => {};

    constructor() {}

    ngOnInit() {
        const input = document.getElementById('address');
        const options = { componentRestrictions: { country: 'be' } };
        this.searchBox = new google.maps.places.Autocomplete(input, options);
        this.searchBox.addListener('place_changed', () => this.updateAddress());
        google.maps.event.addDomListener(input, 'keydown', function(event) {
            if (event.keyCode === 13) {
                event.preventDefault();
            }
        });
    }

    registerOnChange(fn: any): void {
        this.propagateChange = fn;
    }

    registerOnTouched(fn: any): void {}

    setDisabledState(isDisabled: boolean): void {}

    writeValue(obj: any): void {
        if (obj) {
            this.output = obj;
        }
    }

    onChange(event): void {
        if ( !this.textInput ) {
            // reset state
            this.output.street = undefined;
            this.output.city = undefined;
            this.output.zipCode = undefined;
            this.output.province = undefined;
            this.output.latitude = undefined;
            this.output.longitude = undefined;
        }

        this.textInput = event.currentTarget.value;
        // if ( !this.isValidAddress() ) {
        // the address is invalid, try querying places API one more time with given input.
        // if successful, the coordinates will be calculated and the address will be valid afterwards.
        // TODO try to fetch suggestion here manually
        // }
        if ( this.isValidAddress() ) {
            this.invalidAddressError = false;
        } else {
            this.invalidAddressError = true;
        }

        console.log( 'setting address to ' + this.textInput );
        // update the form
        this.propagateChange( this.output );
    }

    // passed or failed from the onChange method
    public validate( c: FormControl ) {
        return this.output && this.isValidAddress() ? null : {
            invalidAddressError: {
                valid: false
            }
        };
    }

    // returns null when valid else the validation object
    // in this case we're checking if the json parsing has

    private isValidAddress() {
        return this.output.latitude !== undefined && this.output.longitude !== undefined;
    }

    private updateAddress() {
        const place = this.searchBox.getPlace();
        if ( place != null ) {
            // as defined in https://developers.google.com/maps/documentation/geocoding/intro#ReverseGeocoding
            this.output.street = place.address_components
                .filter( attr => attr.types.filter( type => type === 'street_number' || type === 'route' ).length > 0 )
                .map( attr => attr.long_name )
                .reverse()
                .join( ' ' );
            this.output.city = place.address_components
                .filter(attr => attr.types.filter(type => type === 'locality').length > 0)
                .map(attr => attr.long_name)
                .join();
            this.output.province = place.address_components
                .filter(attr => attr.types.filter(type => type === 'administrative_area_level_2').length > 0)
                .map(attr => attr.long_name)
                .join();
            this.output.zipCode = place.address_components
                .filter(attr => attr.types.filter(type => type === 'postal_code').length > 0)
                .map(attr => attr.long_name)
                .join();
            if (place.geometry && place.geometry.location) {
                this.output.latitude = place.geometry.location.lat();
                this.output.longitude = place.geometry.location.lng();
            }
            this.output.description = place.formatted_address;
            this.textInput = place.formatted_address;
        }

        // update the form
        this.propagateChange( this.output );
    }
}
