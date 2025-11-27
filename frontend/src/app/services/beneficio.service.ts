import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Beneficio, BeneficioRequest } from '../models/beneficio';
import { Transferencia } from '../models/transferencia';

@Injectable({ providedIn: 'root' })
export class BeneficioService {
  private readonly api = 'http://localhost:8080/api/beneficios';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.api);
  }

  listarAtivos(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(`${this.api}/ativos`);
  }

  buscar(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.api}/${id}`);
  }

  criar(req: BeneficioRequest): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.api, req);
  }

  atualizar(id: number, req: BeneficioRequest): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.api}/${id}`, req);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  transferir(req: Transferencia): Observable<void> {
    return this.http.post<void>(`${this.api}/transferir`, req);
  }
}
