import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {LazyLoadImageModule} from 'ng-lazyload-image';
import {KranzenzoSharedModule} from 'app/shared';
import {SHOP_ROUTE} from 'app/shop/shop.route';
import {OverviewComponent} from 'app/shop/overview.component';
import {ProductDetailComponent} from 'app/shop/product-detail.component';
import {ProductOrderComponent} from 'app/shop/order/product-order.component';
import {CustomerAddressComponent} from 'app/shop/order/customer-address.component';
import {TagOverviewComponent} from 'app/shop/tag-overview.component';
import {PaginatorComponent} from 'app/shop/paginator.component';
import {PickupDateComponent} from 'app/shop/order/pickup-date.component';

@NgModule( {
               imports: [KranzenzoSharedModule, LazyLoadImageModule, RouterModule.forChild( SHOP_ROUTE )],
               declarations: [OverviewComponent, ProductDetailComponent, ProductOrderComponent, CustomerAddressComponent, PickupDateComponent, TagOverviewComponent,
                              PaginatorComponent],
    entryComponents: [ProductOrderComponent]
})
export class ShopModule {}
