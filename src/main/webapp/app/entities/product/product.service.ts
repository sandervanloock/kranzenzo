import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProduct } from 'app/shared/model/product.model';
import { map } from 'rxjs/internal/operators';

type EntityResponseType = HttpResponse<IProduct>;
type EntityArrayResponseType = HttpResponse<IProduct[]>;

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

    search(state: SearchState): Observable<EntityArrayResponseType> {
        return this.http.get<any>(this.resourceUrl + '/search?' + state.toQuery()).pipe(
            map(
                resp =>
                    new HttpResponse({
                        body: resp.content
                    })
            )
        );
        // return Observable.of( new HttpResponse( {body: []} ) );
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

export class SearchState {
    constructor(public query?: string, public tagId?: number, public isActive?: boolean) {}

    toQuery() {
        return `tags=${this.tagId ? this.tagId : ''}&name=${this.query ? this.query : ''}&isActive=${this.isActive ? 'true' : ''}`;
    }
}
