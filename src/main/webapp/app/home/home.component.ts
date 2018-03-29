import {Component, OnInit} from '@angular/core';

import {Principal} from '../shared';
import {JhiEventManager} from 'ng-jhipster';

@Component( {
                selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.css']

            } )
export class HomeComponent implements OnInit {
    account: Account;
    homepageImages: HomepageImage[];

    constructor( private principal: Principal, private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.principal.identity().then( ( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();
        this.homepageImages = [new HomepageImage( 'https://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/1000x500/home_1.jpg', 'Mijn winkel' ),
                               new HomepageImage( 'https://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/1000x500/home_5.jpg', 'Mijn winkel' ),
                               new HomepageImage( 'https://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/1000x500/home_6.jpg', 'Mijn winkel' ),
                               new HomepageImage( 'https://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/1000x500/1522343524-a504e7a6-0287-4114-92f4-52dcc8570481',
                                                  'Mijn winkel' ),
                               new HomepageImage( 'https://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/1000x500/home_11.jpg', 'Mijn winkel' ),];

    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then( ( account ) => {
                this.account = account;
            } );
        } );
    }

}

class HomepageImage {
    constructor( public url: string, public caption?: string, public subcaption?: string ) {
    }
}
