import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Tag} from './tag.model';
import {TagPopupService} from './tag-popup.service';
import {TagService} from './tag.service';
import {Product, ProductService} from '../product';
import {Image, ImageService} from '../image';
import {ResponseWrapper, S3ImageResizePipe} from '../../shared';

@Component( {
                selector: 'jhi-tag-dialog', templateUrl: './tag-dialog.component.html'
            } )
export class TagDialogComponent implements OnInit {

    tag: Tag;
    isSaving: boolean;
    imageEndpoint: string[] = [];

    products: Product[];

    images: Image[];

    constructor( public activeModal: NgbActiveModal,
                 private jhiAlertService: JhiAlertService,
                 private tagService: TagService,
                 private productService: ProductService,
                 private imageService: ImageService,
                 private eventManager: JhiEventManager,
                 private s3ImageResizePipe: S3ImageResizePipe ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.products = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
        this.imageService
            .query( {filter: 'tag-is-null'} )
            .subscribe( ( res: ResponseWrapper ) => {
                if ( !this.tag.image ) {
                    this.images = res.json;
                } else {
                    this.imageService
                        .find( this.tag.image.id )
                        .subscribe( ( subRes: Image ) => {
                            this.images = [subRes].concat( res.json );
                        }, ( subRes: ResponseWrapper ) => this.onError( subRes.json ) );
                }
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
        if ( this.tag.image ) {
            this.imageEndpoint = [this.s3ImageResizePipe.transform( this.tag.image.endpoint, '50x50' )];
        }
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.tag.id !== undefined ) {
            this.subscribeToSaveResponse( this.tagService.update( this.tag ) );
        } else {
            this.subscribeToSaveResponse( this.tagService.create( this.tag ) );
        }
    }

    trackImageById( index: number, item: Image ) {
        return item.id;
    }

    onImageRemoved( $event: any ) {
        this.tag.image = null;
        this.imageEndpoint = [];
    }

    onImageUploadFinished( $event: any ) {
        const id = JSON.parse( $event.serverResponse._body ).id;
        if ( id ) {
            this.tag.image = new Image( id );
            const endpoint = JSON.parse( $event.serverResponse._body ).endpoint;
        }
    }

    private subscribeToSaveResponse( result: Observable<Tag> ) {
        result.subscribe( ( res: Tag ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError() );
    }

    trackProductById( index: number, item: Product ) {
        return item.id;
    }

    private onSaveSuccess( result: Tag ) {
        this.eventManager.broadcast( {name: 'tagListModification', content: 'OK'} );
        this.isSaving = false;
        this.activeModal.dismiss( result );
    }

    getSelected( selectedVals: Array<any>, option: any ) {
        if ( selectedVals ) {
            for ( let i = 0; i < selectedVals.length; i++ ) {
                if ( option.id === selectedVals[i].id ) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError( error: any ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}

@Component( {
                selector: 'jhi-tag-popup', template: ''
            } )
export class TagPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private tagPopupService: TagPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.tagPopupService
                    .open( TagDialogComponent as Component, params['id'] );
            } else {
                this.tagPopupService
                    .open( TagDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
