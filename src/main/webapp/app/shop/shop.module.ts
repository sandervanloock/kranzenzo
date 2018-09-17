import { NgModule } from '@angular/core';
import { KranzenzoSharedModule } from 'app/shared';
import { SHOP_ROUTE } from 'app/shop/shop.route';
import { RouterModule } from '@angular/router';
import { OverviewComponent } from 'app/shop/overview.component';
import { ProductDetailComponent } from 'app/shop/product-detail.component';
import { ProductOrderComponent, ProductOrderPopupComponent } from 'app/shop/order/product-order.component';
import { CustomerAddressComponent } from 'app/shop/order/customer-address.component';
import { TagOverviewComponent } from 'app/shop/tag-overview.component';

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(SHOP_ROUTE)],
    declarations: [
        OverviewComponent,
        ProductDetailComponent,
        ProductOrderComponent,
        ProductOrderPopupComponent,
        CustomerAddressComponent,
        TagOverviewComponent
    ],
    entryComponents: [ProductOrderComponent]
})
export class ShopModule {}
