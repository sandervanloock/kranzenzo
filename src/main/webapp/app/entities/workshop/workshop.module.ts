import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {ImageUploadModule} from 'angular2-image-upload';

import {KransenzoSharedModule, S3ImageResizePipe} from '../../shared';
import {
    WorkshopComponent,
    WorkshopDeleteDialogComponent,
    WorkshopDeletePopupComponent,
    WorkshopDetailComponent,
    WorkshopDialogComponent,
    WorkshopPopupComponent,
    workshopPopupRoute,
    WorkshopPopupService,
    workshopRoute,
    WorkshopService,
} from './';

const ENTITY_STATES = [...workshopRoute, ...workshopPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( ENTITY_STATES ), ImageUploadModule.forRoot()],
               declarations: [WorkshopComponent, WorkshopDetailComponent, WorkshopDialogComponent, WorkshopDeleteDialogComponent, WorkshopPopupComponent,
                              WorkshopDeletePopupComponent,],
               entryComponents: [WorkshopComponent, WorkshopDialogComponent, WorkshopPopupComponent, WorkshopDeleteDialogComponent, WorkshopDeletePopupComponent,],
               providers: [WorkshopService, WorkshopPopupService, S3ImageResizePipe],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoWorkshopModule {
}
