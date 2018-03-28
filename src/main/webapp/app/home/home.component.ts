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
        this.homepageImages = [new HomepageImage( 'https://thescoopdxb.files.wordpress.com/2014/03/20140311_134512-1.jpg', 'Mijn winkel' ),
                               new HomepageImage( 'https://upload.wikimedia.org/wikipedia/commons/2/2f/Flower_Shop%2C_Shad_Thames%2C_London_SE1_-_geograph.org.uk_-_1703738.jpg',
                                                  'Bezoek mijn webshop', 'Deel met al je vrienden' ), new HomepageImage(
                'https://cmkt-image-prd.global.ssl.fastly.net/0.1.0/ps/66249/1160/772/m1/fpnw/wm0/01_creative-market-flower-shop-flyer-templates-kinzi21-.jpg?1390024406&s=dac700063c35a06a6b35c084b2d6f4c6',
                'Ook bonnen te verkrijgen' )];

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
