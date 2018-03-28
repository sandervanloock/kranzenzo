import {Component, OnInit} from '@angular/core';

import {Principal} from '../shared';
import {JhiEventManager} from 'ng-jhipster';

@Component( {
                selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.css']

            } )
export class HomeComponent implements OnInit {
    account: Account;

    constructor( private principal: Principal, private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.principal.identity().then( ( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();

    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then( ( account ) => {
                this.account = account;
            } );
        } );
    }

}
