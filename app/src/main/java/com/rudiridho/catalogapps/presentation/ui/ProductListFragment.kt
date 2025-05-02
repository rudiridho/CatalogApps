package com.rudiridho.catalogapps.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudiridho.catalogapps.R
import com.rudiridho.catalogapps.databinding.FragmentProductListBinding
import com.rudiridho.catalogapps.presentation.adapter.ProductAdapter
import com.rudiridho.catalogapps.presentation.model.ProductUI
import com.rudiridho.catalogapps.presentation.viewmodel.ProductViewModel
import com.rudiridho.catalogapps.utils.UiSafeState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModel()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = binding.swipeRefreshLayout

        setupRecyclerView()
        observeViewModel()
        setupSearchView()
        setupFilterButtons()
        setupRetryButton()
        setupSwipeToRefresh()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            productViewModel.updateProduct(product.copy(isFavorite = !product.isFavorite))
        }
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        productViewModel.products.observe(viewLifecycleOwner) { uiSafeState ->
            handleUiSafeState(uiSafeState)
        }

        productViewModel.favoriteProducts.observe(viewLifecycleOwner) { uiSafeState ->
            handleUiSafeState(uiSafeState)
        }

        productViewModel.searchedProducts.observe(viewLifecycleOwner) { uiSafeState ->
            handleUiSafeState(uiSafeState)
        }
    }

    private fun handleUiSafeState(uiSafeState: UiSafeState<List<ProductUI>>) {
        when (uiSafeState) {
            is UiSafeState.Success -> {
                showProductList(uiSafeState.data)
            }
            is UiSafeState.Loading -> {
                showLoading()
            }
            is UiSafeState.Error -> {
                showError(uiSafeState.message)
            }
            is UiSafeState.ErrorConnection -> {
                showError(getString(R.string.error_connection))
            }
            is UiSafeState.Empty -> {
                showEmptyState()
            }
            else -> {
                // Handle uninitialized state if needed
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    productViewModel.searchProducts(it)
                }
                return true
            }
        })
    }

    private fun setupFilterButtons() {
        binding.btnAll.setOnClickListener {
            productViewModel.getProducts()
        }

        binding.btnFavorites.setOnClickListener {
            productViewModel.getFavoriteProducts()
        }
    }

    private fun setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            productViewModel.getProducts()
        }
    }

    private fun setupRetryButton() {
        binding.errorStateLayout.retryButton.setOnClickListener {
            productViewModel.getProducts()
        }
    }

    private fun showProductList(products: List<ProductUI>) {
        binding.progressBar.visibility = View.GONE
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.errorStateLayout.root.visibility = View.GONE
        binding.rvProducts.visibility = View.VISIBLE
        productAdapter.submitList(products)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.errorStateLayout.root.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.errorStateLayout.root.visibility = View.VISIBLE
        binding.rvProducts.visibility = View.GONE
        binding.errorStateLayout.errorTextView.text = message ?: getString(R.string.error_state_message)
    }

    private fun showEmptyState() {
        binding.progressBar.visibility = View.GONE
        binding.emptyStateLayout.root.visibility = View.VISIBLE
        binding.errorStateLayout.root.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}