import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared';
import {
    WorkshopDateComponent,
    WorkshopDateDeleteDialogComponent,
    WorkshopDateDeletePopupComponent,
    WorkshopDateDetailComponent,
    workshopDatePopupRoute,
    workshopDateRoute,
    WorkshopDateUpdateComponent
} from './';

const ENTITY_STATES = [...workshopDateRoute, ...workshopDatePopupRoute];

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WorkshopDateComponent,
        WorkshopDateDetailComponent,
        WorkshopDateUpdateComponent,
        WorkshopDateDeleteDialogComponent,
        WorkshopDateDeletePopupComponent
    ],
    entryComponents: [
        WorkshopDateComponent,
        WorkshopDateUpdateComponent,
        WorkshopDateDeleteDialogComponent,
        WorkshopDateDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoWorkshopDateModule {}
