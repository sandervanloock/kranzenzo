import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared';
import {
    ProductOrderComponent,
    ProductOrderDeleteDialogComponent,
    ProductOrderDeletePopupComponent,
    ProductOrderDetailComponent,
    productOrderPopupRoute,
    productOrderRoute,
    ProductOrderUpdateComponent
} from './';

const ENTITY_STATES = [...productOrderRoute, ...productOrderPopupRoute];

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductOrderComponent,
        ProductOrderDetailComponent,
        ProductOrderUpdateComponent,
        ProductOrderDeleteDialogComponent,
        ProductOrderDeletePopupComponent
    ],
    entryComponents: [
        ProductOrderComponent,
        ProductOrderUpdateComponent,
        ProductOrderDeleteDialogComponent,
        ProductOrderDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoProductOrderModule {}
