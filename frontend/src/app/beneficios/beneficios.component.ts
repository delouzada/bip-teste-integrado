import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Beneficio, BeneficioRequest } from '../models/beneficio';
import { BeneficioService } from '../services/beneficio.service';

@Component({
  selector: 'app-beneficios',
  templateUrl: './beneficios.component.html',
  styleUrls: ['./beneficios.component.scss']
})
export class BeneficiosComponent implements OnInit {
  beneficios: Beneficio[] = [];
  form: FormGroup;
  carregando = false;
  salvando = false;
  mensagem?: string;
  mensagemTipo: 'success' | 'error' | undefined;
  editandoId: number | null = null;

  constructor(private fb: FormBuilder, private service: BeneficioService) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(2)]],
      descricao: [''],
      valor: [null, [Validators.required, Validators.min(0.01)]],
      ativo: [true]
    });
  }

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.service.listarTodos().subscribe({
      next: (data) => {
        this.beneficios = data;
      },
      error: () => {
        this.mensagem = 'Erro ao carregar beneficios.';
        this.mensagemTipo = 'error';
      }
    }).add(() => (this.carregando = false));
  }

  editar(item: Beneficio): void {
    this.editandoId = item.id;
    this.form.patchValue({
      nome: item.nome,
      descricao: item.descricao,
      valor: item.valor,
      ativo: item.ativo
    });
    this.mensagem = undefined;
  }

  cancelarEdicao(): void {
    this.editandoId = null;
    this.form.reset({ ativo: true });
  }

  salvar(): void {
    if (this.form.invalid) {
      this.mensagem = 'Preencha os campos obrigatorios.';
      this.mensagemTipo = 'error';
      return;
    }

    this.salvando = true;
    this.mensagem = undefined;
    const payload: BeneficioRequest = this.form.value;

    const obs = this.editandoId
      ? this.service.atualizar(this.editandoId, payload)
      : this.service.criar(payload);

    obs.subscribe({
      next: () => {
        this.mensagem = this.editandoId ? 'Beneficio atualizado.' : 'Beneficio criado.';
        this.mensagemTipo = 'success';
        this.cancelarEdicao();
        this.carregar();
      },
      error: (err) => {
        this.mensagem = err?.error?.message || 'Erro ao salvar beneficio.';
        this.mensagemTipo = 'error';
      }
    }).add(() => (this.salvando = false));
  }

  excluir(id: number): void {
    if (!confirm('Confirma excluir este beneficio?')) {
      return;
    }
    this.service.excluir(id).subscribe({
      next: () => {
        this.mensagem = 'Beneficio excluido.';
        this.mensagemTipo = 'success';
        this.carregar();
      },
      error: (err) => {
        this.mensagem = err?.error?.message || 'Erro ao excluir beneficio.';
        this.mensagemTipo = 'error';
      }
    });
  }
}
