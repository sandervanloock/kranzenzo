import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiDataUtils, JhiEventManager} from 'ng-jhipster';

import {Image} from './image.model';
import {ImageService} from './image.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-image', templateUrl: './image.component.html', styleUrls: ['image-component.css']
            } )
export class ImageComponent implements OnInit, OnDestroy {
    images: Image[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private imageService: ImageService,
        private alertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.imageService.search( {
                                          query: this.currentSearch,
                                      } ).subscribe( ( res: ResponseWrapper ) => this.images = res.json, ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.imageService.query().subscribe( ( res: ResponseWrapper ) => {
            this.images = res.json;
            this.currentSearch = '';
        }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    search( query ) {
        if ( !query ) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then( ( account ) => {
            this.currentAccount = account;
        } );
        this.registerChangeInImages();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: Image ) {
        return item.id;
    }

    byteSize( field ) {
        return this.dataUtils.byteSize( field );
    }

    openFile( contentType, field ) {
        return this.dataUtils.openFile( contentType, field );
    }

    registerChangeInImages() {
        this.eventSubscriber = this.eventManager.subscribe( 'imageListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.alertService.error( error.message, null, null );
    }
}
