import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    LocationComponent,
    LocationDeleteDialogComponent,
    LocationDeletePopupComponent,
    LocationDetailComponent,
    LocationDialogComponent,
    LocationPopupComponent,
    locationPopupRoute,
    LocationPopupService,
    locationRoute,
    LocationService,
} from './';

const ENTITY_STATES = [...locationRoute, ...locationPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forRoot( ENTITY_STATES, {useHash: true} )],
               declarations: [LocationComponent, LocationDetailComponent, LocationDialogComponent, LocationDeleteDialogComponent, LocationPopupComponent,
                              LocationDeletePopupComponent,],
               entryComponents: [LocationComponent, LocationDialogComponent, LocationPopupComponent, LocationDeleteDialogComponent, LocationDeletePopupComponent,],
               providers: [LocationService, LocationPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoLocationModule {
}
