import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    ImageComponent,
    ImageDeleteDialogComponent,
    ImageDeletePopupComponent,
    ImageDetailComponent,
    ImageDialogComponent,
    ImagePopupComponent,
    imagePopupRoute,
    ImagePopupService,
    imageRoute,
    ImageService,
} from './';

const ENTITY_STATES = [...imageRoute, ...imagePopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forRoot( ENTITY_STATES, {useHash: true} )],
               declarations: [ImageComponent, ImageDetailComponent, ImageDialogComponent, ImageDeleteDialogComponent, ImagePopupComponent, ImageDeletePopupComponent,],
               entryComponents: [ImageComponent, ImageDialogComponent, ImagePopupComponent, ImageDeleteDialogComponent, ImageDeletePopupComponent,],
               providers: [ImageService, ImagePopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoImageModule {
}
