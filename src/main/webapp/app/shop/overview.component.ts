import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';
import { Principal } from 'app/core';
import { ProductService } from 'app/entities/product';
import { IProduct, Product } from 'app/shared/model/product.model';
import { HttpResponse } from '@angular/common/http';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';

class SearchState {
    constructor(public query?: string, public tagId?: number) {}

    toQuery() {
        return `tag=${this.tagId ? this.tagId : ''}&query=${this.query ? this.query : ''}`;
    }
}

@Component({
    selector: 'jhi-overview',
    templateUrl: './overview.component.html',
    styleUrls: ['overview.css']
})
export class OverviewComponent implements OnInit {
    items: Product[] = [];
    allItems: Product[] = [];
    searchState: SearchState;

    tags: ITag[];

    constructor(
        private principal: Principal,
        private productService: ProductService,
        private route: ActivatedRoute,
        private tagService: TagService,
        private location: Location
    ) {}

    ngOnInit() {
        const queryParams: Params = Object.assign({}, this.route.snapshot.queryParams);
        this.searchState = new SearchState(queryParams['query'], parseInt(queryParams['tag'], 10));
        this.loadProducts();
        this.tagService.query().subscribe((res: HttpResponse<ITag[]>) => {
            this.tags = res.body;
        });
    }

    loadProducts() {
        const req = { activeOnly: true };
        if (this.searchState.tagId) {
            req['tag'] = this.searchState.tagId;
        }
        this.productService.query(req).subscribe((data: HttpResponse<IProduct[]>) => {
            this.items = data.body;
            this.allItems = data.body;
            this.reloadState();
        });
    }

    reloadState() {
        let filteredItems = this.allItems;
        if (this.searchState.tagId) {
            filteredItems = filteredItems.filter(item => {
                return item.tags.findIndex(tag => tag.id === this.searchState.tagId) !== -1;
            });
        }
        if (this.searchState.query) {
            filteredItems = filteredItems.filter(item => {
                return item.name
                    ? item.name.toLowerCase().includes(this.searchState.query.toLowerCase())
                    : item.description ? item.description.toLowerCase().includes(this.searchState.query.toLowerCase()) : false;
            });
        }
        this.items = filteredItems;
        this.location.go('shop', this.searchState.toQuery());
    }
}
