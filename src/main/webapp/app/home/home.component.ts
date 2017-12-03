import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';
import {Http, Response} from '@angular/http';

class Product {
    constructor( public img?: string ) {
    }
}

@Component( {
                selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.css']

            } )
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    items: Product[] = [];
    groupedItems: Product[][] = [];
    dimensions: String[] = ['640x300'];
    MAX_ITEMS_PER_ROW = 2;

    constructor( private principal: Principal, private loginModalService: LoginModalService, private eventManager: JhiEventManager, private http: Http ) {
    }

    ngOnInit() {
        this.principal.identity().then( ( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();
        this.http.get( 'https://www.googleapis.com/books/v1/volumes?q=inauthor:Ernest+Hemingway' )
            .map( ( res: Response ) => res.json().items.forEach( ( item ) => this.append( item.volumeInfo.title, item.volumeInfo.description ) ) )
            .subscribe( () => {
                this.items.forEach( ( item ) => {
                    let i = Math.floor( (Math.random() * this.groupedItems.length) + 0.5 );
                    while ( this.groupedItems[i] !== undefined && this.groupedItems[i].length >= this.MAX_ITEMS_PER_ROW ) {
                        i = Math.floor( (Math.random() * this.groupedItems.length) + 0.5 );
                    }
                    if ( !this.groupedItems[i] ) {
                        this.groupedItems[i] = [];
                    }
                    this.groupedItems[i].push( item );
                } )
            } );
    }

    append( title, content ) {
        const dimension = this.dimensions[Math.floor( Math.random() * this.dimensions.length - 1 ) + 1];
        this.items.push( new Product( `http://via.placeholder.com/${dimension}` ) );
    };

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
