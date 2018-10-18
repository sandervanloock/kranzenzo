import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { ProductService } from 'app/entities/product';
import { IProduct, Product } from 'app/shared/model/product.model';
import { HttpResponse } from '@angular/common/http';

class SearchState {
    query: string;
}

@Component({
    selector: 'jhi-overview',
    templateUrl: './overview.component.html',
    styleUrls: ['overview.css']
})
export class OverviewComponent implements OnInit {
    tagName: string;
    items: Product[] = [];
    allItems: Product[] = [];
    searchState: SearchState = new SearchState();

    constructor(private principal: Principal, private productService: ProductService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            if (params.tag) {
                this.tagName = params.tag;
            }
            this.loadProducts();
        });
    }

    loadProducts() {
        const req = { activeOnly: true };
        if (this.tagName) {
            req['tagName'] = this.tagName;
        }
        this.productService.query(req).subscribe((data: HttpResponse<IProduct[]>) => {
            this.items = data.body;
            this.allItems = data.body;
        });
    }

    reloadState() {
        this.items = this.allItems.filter(item => {
            return item.name ? item.name.toLowerCase().includes(this.searchState.query.toLowerCase()) : false;
            //      item.description ? item.description.toLowerCase().includes( this.searchState.query.toLowerCase()) : false;
        });
        console.log(this.items.length);
    }
}
