package com.example.database

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class FilterRequest(
    var createTimeStart: Date? = null,
    var createTimeEnd: Date? = null,
    var parentId: Long? = null,
    var parentColumn: String? = null,
    var status: Int? = null,
    var statusColumn: String? = null,
    var keywords: String? = null,
    var filterColumns: String? = null,
    var deleted: Int = -1,

    private var pageIndex: Int = 1,
    private var pageSize: Int = 15,
    private var sorts: String? = null
) {
    fun pageRequest(): PageRequest {
        return sorts?.let {
            PageRequest.of(pageIndex - 1, pageSize, sort())
        } ?: let {
            PageRequest.of(pageIndex - 1, pageSize)
        }
    }

    private fun sort(): Sort {
        val orderList: MutableList<Sort.Order> = ArrayList()
        sorts?.let {
            val orders = it.split(",")
            for (order in orders) {
                val columnDirection = order.trim().split(" ")
                if (columnDirection.size == 2) {
                    val direction = if (columnDirection[1] == "desc") Sort.Direction.DESC else Sort.Direction.ASC
                    orderList.add(Sort.Order.by(columnDirection[0].trim()).with(direction))
                } else {
                    orderList.add(Sort.Order.by(columnDirection[0].trim()).with(Sort.Direction.DESC))
                }
            }
        } ?: let {
            orderList.add(Sort.Order.by("createTime").with(Sort.Direction.DESC))
        }
        return Sort.by(orderList)
    }
}

class FilterConfig(
    var parentColumn: String? = null,
    var statusColumn: String? = null,
    var filterColumns: String? = null
)

class FilterSpecification<T>(val request: FilterRequest, val config: FilterConfig? = null) : Specification<T> {

    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate {
        val predicates: MutableList<Predicate> = ArrayList()

        request.createTimeStart?.let {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createTime"), it))
        }
        request.createTimeEnd?.let {
            predicates.add(builder.lessThan(root.get("createTime"), it))
        }

        request.parentId?.let { parentId ->
            config?.parentColumn?.let {
                predicates.add(builder.equal(root.get<Long>(it), parentId))
            } ?: let {
                request.parentColumn?.let {
                    predicates.add(builder.equal(root.get<Long>(it), parentId))
                }
            }
        }

        request.status?.let { status ->
            config?.statusColumn?.let {
                predicates.add(builder.equal(root.get<Int>(it), status))
            } ?: let {
                request.statusColumn?.let {
                    predicates.add(builder.equal(root.get<Int>(it), status))
                }
            }
        }

        request.keywords?.let { keywords ->
            var filterColumns: List<String>? = null
            config?.filterColumns?.let {
                filterColumns = it.split(",")
            } ?: let {
                request.filterColumns?.let {
                    filterColumns = it.split(",")
                }
            }
            filterColumns?.let {
                val predicatesKeyword: MutableList<Predicate> = ArrayList()
                for (keyword in keywords.split(" ")) {
                    for (filterColumn in it) {
                        predicatesKeyword.add(builder.like(root.get(filterColumn), "%$keyword%"))
                    }
                }
                predicates.add(builder.or(*predicatesKeyword.toTypedArray()))
            }
        }

        if (request.deleted > 0) {
            predicates.add(builder.isNotNull(root.get<Date?>("deleteTime")))
        } else if (request.deleted < 0) {
            predicates.add(builder.isNull(root.get<Date?>("deleteTime")))
        }

        return builder.and(*predicates.toTypedArray())
    }
}
