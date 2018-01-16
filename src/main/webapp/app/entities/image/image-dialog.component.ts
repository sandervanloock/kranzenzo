import {Component, ElementRef, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiDataUtils, JhiEventManager} from 'ng-jhipster';

import {Image} from './image.model';
import {ImagePopupService} from './image-popup.service';
import {ImageService} from './image.service';
import {Product, ProductService} from '../product';
import {ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-image-dialog', templateUrl: './image-dialog.component.html'
            } )
export class ImageDialogComponent implements OnInit {

    image: Image;
    isSaving: boolean;

    products: Product[];

    constructor( public activeModal: NgbActiveModal,
                 private dataUtils: JhiDataUtils,
                 private jhiAlertService: JhiAlertService,
                 private imageService: ImageService,
                 private productService: ProductService,
                 private elementRef: ElementRef,
                 private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.products = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    byteSize( field ) {
        return this.dataUtils.byteSize( field );
    }

    openFile( contentType, field ) {
        return this.dataUtils.openFile( contentType, field );
    }

    setFileData( event, entity, field, isImage ) {
        this.dataUtils.setFileData( event, entity, field, isImage );
    }

    clearInputImage( field: string, fieldContentType: string, idInput: string ) {
        this.dataUtils.clearInputImage( this.image, this.elementRef, field, fieldContentType, idInput );
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.image.id !== undefined ) {
            this.subscribeToSaveResponse( this.imageService.update( this.image ) );
        } else {
            this.subscribeToSaveResponse( this.imageService.create( this.image ) );
        }
    }

    trackProductById( index: number, item: Product ) {
        return item.id;
    }

    private subscribeToSaveResponse( result: Observable<Image> ) {
        result.subscribe( ( res: Image ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError() );
    }

    private onSaveSuccess( result: Image ) {
        this.eventManager.broadcast( {name: 'imageListModification', content: 'OK'} );
        this.isSaving = false;
        this.activeModal.dismiss( result );
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError( error: any ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}

@Component( {
                selector: 'jhi-image-popup', template: ''
            } )
export class ImagePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private imagePopupService: ImagePopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.imagePopupService
                    .open( ImageDialogComponent as Component, params['id'] );
            } else {
                this.imagePopupService
                    .open( ImageDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
