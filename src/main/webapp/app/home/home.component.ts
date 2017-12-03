import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';
import {Http} from '@angular/http';
import {Product} from './home-product.component';

@Component( {
                selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.css']

            } )
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    items: Product[] = [];
    slicedItems: Product[][] = [];

    constructor( private principal: Principal, private loginModalService: LoginModalService, private eventManager: JhiEventManager, private http: Http ) {
    }

    ngOnInit() {
        this.principal.identity().then( ( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();
        this.http.get( 'https://www.googleapis.com/books/v1/volumes?q=inauthor:Ernest+Hemingway' )
            .map( ( res: any ) => {
                return res.json().items.forEach( () => this.items.push( new Product( 'http://via.placeholder.com/420x300' ) ) );
            } )
            .subscribe( () => {
                const chunk = 2;
                for ( let i = 0; i < this.items.length; i += chunk ) {
                    this.slicedItems.push( this.items.slice( i, i + chunk ) );
                }
            } );
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then( ( account ) => {
                this.account = account;
            } );
        } );
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
