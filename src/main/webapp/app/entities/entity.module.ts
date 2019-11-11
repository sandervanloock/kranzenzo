import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { KranzenzoProductModule } from './product/product.module';
import { KranzenzoImageModule } from './image/image.module';
import { KranzenzoTagModule } from './tag/tag.module';
import { KranzenzoCustomerModule } from './customer/customer.module';
import { KranzenzoProductOrderModule } from './product-order/product-order.module';
import { KranzenzoLocationModule } from './location/location.module';
import { KranzenzoWorkshopModule } from './workshop/workshop.module';
import { KranzenzoWorkshopDateModule } from './workshop-date/workshop-date.module';
import { KranzenzoWorkshopSubscriptionModule } from './workshop-subscription/workshop-subscription.module';
import { HomepageModule } from 'app/entities/homepage/homepage.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [KranzenzoProductModule, KranzenzoImageModule, KranzenzoTagModule, KranzenzoCustomerModule, KranzenzoProductOrderModule, KranzenzoLocationModule,
              KranzenzoWorkshopModule, KranzenzoWorkshopDateModule, KranzenzoWorkshopSubscriptionModule, HomepageModule,
                   /* jhipster-needle-add-entity-module - JHipster will add entity modules here */],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoEntityModule {}
