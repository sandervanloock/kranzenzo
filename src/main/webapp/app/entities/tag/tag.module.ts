import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared';
import {
    TagComponent,
    TagDeleteDialogComponent,
    TagDeletePopupComponent,
    TagDetailComponent,
    tagPopupRoute,
    tagRoute,
    TagUpdateComponent
} from './';

const ENTITY_STATES = [...tagRoute, ...tagPopupRoute];

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [TagComponent, TagDetailComponent, TagUpdateComponent, TagDeleteDialogComponent, TagDeletePopupComponent],
    entryComponents: [TagComponent, TagUpdateComponent, TagDeleteDialogComponent, TagDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoTagModule {}
