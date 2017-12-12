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

    constructor() {
    }

    ngOnInit() {
        this.step = 1;
    }

}
