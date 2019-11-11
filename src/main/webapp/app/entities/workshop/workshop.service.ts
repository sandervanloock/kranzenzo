import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IWorkshop } from 'app/shared/model/workshop.model';

type EntityResponseType = HttpResponse<IWorkshop>;
type EntityArrayResponseType = HttpResponse<IWorkshop[]>;

@Injectable({ providedIn: 'root' })
export class WorkshopService {
  public resourceUrl = SERVER_API_URL + 'api/workshops';

  constructor(protected http: HttpClient) {}

  create(workshop: IWorkshop): Observable<EntityResponseType> {
    return this.http.post<IWorkshop>(this.resourceUrl, workshop, { observe: 'response' });
  }

  update(workshop: IWorkshop): Observable<EntityResponseType> {
    return this.http.put<IWorkshop>(this.resourceUrl, workshop, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkshop>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkshop[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
