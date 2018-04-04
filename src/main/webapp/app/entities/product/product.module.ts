import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {ImageUploadModule} from 'angular2-image-upload';

import {KransenzoSharedModule, S3ImageResizePipe} from '../../shared';
import {
    ProductComponent,
    ProductDeleteDialogComponent,
    ProductDeletePopupComponent,
    ProductDetailComponent,
    ProductDialogComponent,
    ProductPopupComponent,
    productPopupRoute,
    ProductPopupService,
    productRoute,
    ProductService,
} from './';

const ENTITY_STATES = [...productRoute, ...productPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forRoot( ENTITY_STATES, {useHash: true} ), ImageUploadModule.forRoot(),],
               declarations: [ProductComponent, ProductDetailComponent, ProductDialogComponent, ProductDeleteDialogComponent, ProductPopupComponent, ProductDeletePopupComponent,],
               entryComponents: [ProductComponent, ProductDialogComponent, ProductPopupComponent, ProductDeleteDialogComponent, ProductDeletePopupComponent,],
               providers: [ProductService, ProductPopupService, S3ImageResizePipe],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoProductModule {
}
