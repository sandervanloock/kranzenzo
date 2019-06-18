import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProduct } from 'app/shared/model/product.model';
import { map } from 'rxjs/internal/operators';
import { MatPaginatorIntl } from '@angular/material';

type EntityResponseType = HttpResponse<IProduct>;
type EntityArrayResponseType = HttpResponse<IProduct[]>;
type EntityPageResponseType = HttpResponse<Page<IProduct>>;

@Injectable({ providedIn: 'root' })
export class ProductService {
    private resourceUrl = SERVER_API_URL + 'api/products';

    constructor(private http: HttpClient) {}

    create(product: IProduct): Observable<EntityResponseType> {
        return this.http.post<IProduct>(this.resourceUrl, product, { observe: 'response' });
    }

    update(product: IProduct): Observable<EntityResponseType> {
        return this.http.put<IProduct>(this.resourceUrl, product, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    search(state: SearchState): Observable<EntityPageResponseType> {
        return this.http.get<any>(this.resourceUrl + '/search?' + state.toQuery()).pipe(
            map(
                resp =>
                    new HttpResponse({
                        body: new Page(resp.number, resp.totalElements, resp.content)
                    })
            )
        );
        // return Observable.of( new HttpResponse( {body: []} ) );
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

export class Page<T> {
    constructor(public number: number, public size: number, public content: T[]) {}
}

class Pageable {
    constructor(public page?: number, public pageSize?: number) {}

    toQuery() {
        let query = '';
        if (this.page) {
            query += `page=${this.page}&`;
        }
        if (this.pageSize) {
            query += `pageSize=${this.pageSize}&`;
        }
        return query;
    }
}

export class SearchState extends Pageable {
    constructor(public page?: number, public name?: string, public tags?: number, public isActive?: boolean, public sort?: string) {
        super();
    }

    toQuery() {
        return (
            super.toQuery() +
            `tags=${this.tags ? this.tags : ''}&name=${this.name ? this.name : ''}&isActive=${this.isActive ? '1' : ''}&sort=${
                this.sort ? this.sort : ''
            }`
        );
    }
}

export class MatPaginatorIntlDutch extends MatPaginatorIntl {
    itemsPerPageLabel = 'Producten per pagina';
    nextPageLabel = 'Volgende pagina';
    previousPageLabel = 'Vorige pagina';

    getRangeLabel = function(page, pageSize, length) {
        if (length === 0 || pageSize === 0) {
            return '0 tot ' + length;
        }
        length = Math.max(length, 0);
        const startIndex = page * pageSize;
        // If the start index exceeds the list length, do not try and fix the end index to the end.
        const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
        return startIndex + 1 + ' - ' + endIndex + ' tot ' + length;
    };
}
