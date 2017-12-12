import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    TagComponent,
    TagDeleteDialogComponent,
    TagDeletePopupComponent,
    TagDetailComponent,
    TagDialogComponent,
    TagPopupComponent,
    tagPopupRoute,
    TagPopupService,
    tagRoute,
    TagService,
} from './';

const ENTITY_STATES = [...tagRoute, ...tagPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forRoot( ENTITY_STATES, {useHash: true} )],
               declarations: [TagComponent, TagDetailComponent, TagDialogComponent, TagDeleteDialogComponent, TagPopupComponent, TagDeletePopupComponent,],
               entryComponents: [TagComponent, TagDialogComponent, TagPopupComponent, TagDeleteDialogComponent, TagDeletePopupComponent,],
               providers: [TagService, TagPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoTagModule {
}
