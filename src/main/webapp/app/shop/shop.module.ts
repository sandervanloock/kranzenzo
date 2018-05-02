import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {OverviewComponent} from './overview.component';
import {RouterModule} from '@angular/router';
import {ShareButtonsModule} from 'ngx-sharebuttons';
import {SHOP_ROUTE} from './shop.route';
import {ProductDetailComponent} from './product-detail.component';
import {KransenzoSharedModule} from '../shared';
import {ProductOrderComponent, ProductOrderPopupComponent} from './order/product-order.component';
import {CustomerAddressComponent} from './order/customer-address.component';
import {TagOverviewComponent} from './tag-overview.component';

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( SHOP_ROUTE ), HttpClientModule, ShareButtonsModule.forRoot(),],
               declarations: [OverviewComponent, ProductDetailComponent, ProductOrderComponent, ProductOrderPopupComponent, CustomerAddressComponent, TagOverviewComponent],
               entryComponents: [ProductOrderComponent]
           } )
export class KransenzoShopModule {
}
