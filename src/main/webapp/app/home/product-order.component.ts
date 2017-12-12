import {Component, OnInit} from '@angular/core';

/*
Based on https://codepen.io/bryceyork/pen/MyPjPE

Inspired by https://www.google.com/design/spec/components/steppers.html#steppers-types-of-steppers

And leveraging the Creative Tim Material Bootstrap Library - http://demos.creative-tim.com/material-kit/index.html
 */
@Component( {
                selector: 'jhi-product-order', templateUrl: './product-order.component.html', styleUrls: ['home.css']
            } )
export class ProductOrderComponent implements OnInit {
    step: number;
    deliveryType: string;
    led: boolean;
    price: number;

    constructor() {
    }

    ngOnInit() {
        this.step = 1;
        this.price = 50;
        this.deliveryType = 'pickup';
    }

    onSelectionChange( type: string ) {
        if ( this.deliveryType === 'pickup' && type === 'delivered' ) {
            this.price += 5;
        } else if ( this.deliveryType === 'delivered' && type === 'pickup' ) {
            this.price -= 5;
        }
        this.deliveryType = type;
    }

    toggleLed() {
        this.led = !this.led;
        if ( this.led ) {
            this.price += 0.5;
        } else {
            this.price -= 0.5;
        }
    }
}
