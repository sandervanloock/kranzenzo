import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.KranzenzoProductModule)
      },
      {
        path: 'image',
        loadChildren: () => import('./image/image.module').then(m => m.KranzenzoImageModule)
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.KranzenzoTagModule)
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.KranzenzoCustomerModule)
      },
      {
        path: 'product-order',
        loadChildren: () => import('./product-order/product-order.module').then(m => m.KranzenzoProductOrderModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.KranzenzoLocationModule)
      },
      {
        path: 'workshop',
        loadChildren: () => import('./workshop/workshop.module').then(m => m.KranzenzoWorkshopModule)
      },
      {
        path: 'workshop-date',
        loadChildren: () => import('./workshop-date/workshop-date.module').then(m => m.KranzenzoWorkshopDateModule)
      },
      {
        path: 'workshop-subscription',
        loadChildren: () => import('./workshop-subscription/workshop-subscription.module').then(m => m.KranzenzoWorkshopSubscriptionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class KranzenzoEntityModule {}
